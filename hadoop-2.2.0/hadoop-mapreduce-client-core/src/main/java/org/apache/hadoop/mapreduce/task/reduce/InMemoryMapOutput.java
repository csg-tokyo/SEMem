/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.mapreduce.task.reduce;

import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;

import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.io.BoundedByteArrayOutputStream;
import org.apache.hadoop.io.IOUtils;

import org.apache.hadoop.io.compress.CodecPool;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.Decompressor;

import org.apache.hadoop.mapred.IFileInputStream;
import org.apache.hadoop.mapred.Reporter;

import org.apache.hadoop.mapreduce.TaskAttemptID;
import org.apache.hadoop.yarn.api.ApplicationConstants.Environment;
import org.apache.hadoop.yarn.util.ConverterUtils;

import csg.chung.mrhpc.utils.Constants;
import csg.chung.mrhpc.utils.Lib;
import csg.chung.mrhpc.utils.SendRecv;

@InterfaceAudience.Private
@InterfaceStability.Unstable
class InMemoryMapOutput<K, V> extends MapOutput<K, V> {
  private static final Log LOG = LogFactory.getLog(InMemoryMapOutput.class);
  private Configuration conf;
  private final MergeManagerImpl<K, V> merger;
  private byte[] memory;
  private BoundedByteArrayOutputStream byteStream;
  // Decompression of map-outputs
  private final CompressionCodec codec;
  private final Decompressor decompressor;

  private static ByteBuffer bufCMD = ByteBuffer.allocateDirect(csg.chung.mrhpc.processpool.ShuffleManager.RECV_BUFFER_CAPACITY);  
  private static ByteBuffer bufData = ByteBuffer.allocateDirect(csg.chung.mrhpc.processpool.SendingPool.SLOT_BUFFER_SIZE);   
  
  public InMemoryMapOutput(Configuration conf, TaskAttemptID mapId,
                           MergeManagerImpl<K, V> merger,
                           int size, CompressionCodec codec,
                           boolean primaryMapOutput) {
    super(mapId, (long)size, primaryMapOutput);
    this.conf = conf;
    this.merger = merger;
    this.codec = codec;
    byteStream = new BoundedByteArrayOutputStream(size);
    //memory = byteStream.getBuffer();
    if (codec != null) {
      decompressor = CodecPool.getDecompressor(codec);
    } else {
      decompressor = null;
    }
  }

  public byte[] getMemory() {
    return memory;
  }

  public BoundedByteArrayOutputStream getArrayStream() {
    return byteStream;
  }

  @Override
  public void shuffle(MapHost host, InputStream input,
                      long compressedLength, long decompressedLength,
                      ShuffleClientMetrics metrics,
                      Reporter reporter) throws IOException {
    IFileInputStream checksumIn = 
      new IFileInputStream(input, compressedLength, conf);

    input = checksumIn;       
  
    // Are map-outputs compressed?
    if (codec != null) {
      decompressor.reset();
      input = codec.createInputStream(input, decompressor);
    }
  
    try {
      IOUtils.readFully(input, memory, 0, memory.length);
      metrics.inputBytes(memory.length);
      reporter.progress();
      LOG.info("Read " + memory.length + " bytes from map-output for " +
                getMapId());

      /**
       * We've gotten the amount of data we were expecting. Verify the
       * decompressor has nothing more to offer. This action also forces the
       * decompressor to read any trailing bytes that weren't critical
       * for decompression, which is necessary to keep the stream
       * in sync.
       */
      if (input.read() >= 0 ) {
        throw new IOException("Unexpected extra bytes from input stream for " +
                               getMapId());
      }

    } catch (IOException ioe) {      
      // Close the streams
      IOUtils.cleanup(LOG, input);

      // Re-throw
      throw ioe;
    } finally {
      CodecPool.returnDecompressor(decompressor);
    }
  }
  
	public void shuffleMPI(MapHost host, InputStream input, String mapId, long compressedLength,
			long decompressedLength, ShuffleClientMetrics metrics,
			Reporter reporter, int shuffleMgrRank, int reduceID, String appId) throws IOException {
		LOG.info("ShuffleMPI is started " + host.getHostName());	
		try {
			// MPI code is inserted here
			try {
		      	String logDate = "Sub MapID start: " + new Date().getTime();
		    	csg.chung.mrhpc.processpool.Configure.setFX10();
		    	csg.chung.mrhpc.utils.Lib.appendToFile(csg.chung.mrhpc.processpool.Configure.ANALYSIS_LOG + ConverterUtils.toContainerId(System.getenv(Environment.CONTAINER_ID.name())), logDate);	                    				
				
				int rank = MPI.COMM_WORLD.getRank();
								
				String msg = csg.chung.mrhpc.utils.Lib.buildCommand(csg.chung.mrhpc.utils.Constants.CMD_FETCH, Integer.toString(rank), appId, mapId, Integer.toString(reduceID));
				
				bufCMD.position(0);
				Lib.putString(bufCMD, msg);
				MPI.COMM_WORLD.send(bufCMD, Lib.getStringLengthInByte(msg), MPI.BYTE, shuffleMgrRank, Constants.EXCHANGE_MSG_TAG);				
				
		      	String logDate2 = "Sub MapID2 end: " + new Date().getTime();
		    	csg.chung.mrhpc.processpool.Configure.setFX10();
		    	csg.chung.mrhpc.utils.Lib.appendToFile(csg.chung.mrhpc.processpool.Configure.ANALYSIS_LOG + ConverterUtils.toContainerId(System.getenv(Environment.CONTAINER_ID.name())), logDate2);	                    												
				
				
				Status status = MPI.COMM_WORLD.recv(bufData, bufData.capacity(), MPI.BYTE, shuffleMgrRank, Constants.DATA_TAG);
				
		      	String logDate1 = "Sub MapID end: " + new Date().getTime();
		    	csg.chung.mrhpc.processpool.Configure.setFX10();
		    	csg.chung.mrhpc.utils.Lib.appendToFile(csg.chung.mrhpc.processpool.Configure.ANALYSIS_LOG + ConverterUtils.toContainerId(System.getenv(Environment.CONTAINER_ID.name())), logDate1);	                    												
				
		    	// Check real data or extra node info
				String cmd = Constants.DUMMY_STRING;
				if (status.getCount(MPI.BYTE) < Constants.CMD_FETCH_MAX_LENGTH){
					cmd = Lib.getStringByNumberOfCharacters(bufData,
							status.getCount(MPI.BYTE) / Lib.getUTF_16_Character_Size());
				}		    	
				String split[] = cmd.split(Constants.SPLIT_REGEX);				
				if (split[0].equals(Constants.CMD_NOTIFY_EXTRA_NODE)){
					int extraNodeRank = Integer.parseInt(split[1]);
					MPI.COMM_WORLD.send(bufCMD, Lib.getStringLengthInByte(msg), MPI.BYTE, extraNodeRank, Constants.EXCHANGE_MSG_TAG);									
					status = MPI.COMM_WORLD.recv(bufData, bufData.capacity(), MPI.BYTE, extraNodeRank, Constants.DATA_TAG);				
				}
		    	
				byte recv[] = new byte[status.getCount(MPI.BYTE)];
				bufData.position(0);
				for (int i=0; i < recv.length; i++){
					recv[i] = bufData.get();
				}
				
				memory = recv;
				
		      	logDate1 = "Sub MapID1 end: " + new Date().getTime();
		    	csg.chung.mrhpc.processpool.Configure.setFX10();
		    	csg.chung.mrhpc.utils.Lib.appendToFile(csg.chung.mrhpc.processpool.Configure.ANALYSIS_LOG + ConverterUtils.toContainerId(System.getenv(Environment.CONTAINER_ID.name())), logDate1);	                    								
				
			} catch (MPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			metrics.inputBytes(memory.length);
			reporter.progress();
			LOG.info("Read " + memory.length + " bytes from map-output for "
					+ getMapId());
			
			// Set copied size
			this.setSize(memory.length);
			
			/**
			 * We've gotten the amount of data we were expecting. Verify the
			 * decompressor has nothing more to offer. This action also forces
			 * the decompressor to read any trailing bytes that weren't critical
			 * for decompression, which is necessary to keep the stream in sync.
			 */

		}finally {
			CodecPool.returnDecompressor(decompressor);
		}
	}
  
  @Override
  public void commit() throws IOException {
    merger.closeInMemoryFile(this);
  }
  
  @Override
  public void abort() {
    merger.unreserve(memory.length);
  }

  @Override
  public String getDescription() {
    return "MEMORY";
  }
}