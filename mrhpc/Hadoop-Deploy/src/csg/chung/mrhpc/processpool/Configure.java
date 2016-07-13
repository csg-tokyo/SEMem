package csg.chung.mrhpc.processpool;

public class Configure {
	/**
	 * Running file
	 */
	public static String HADOOP_TAR_GZ_FILE 		= "/group/gc83/c83014/hadoop-mpi-inmemory/deploy/hadoop.tar.gz";	
	
	/**
	 * Deploying directory for whole data: source code, storage, logs, and so on. 
	 * Note: don't add / in the end of the path
	 */
	public static String DEPLOY_FOLDER 			= "/group/gc83/c83014/hadoop-mpi-inmemory/deploy";
	public static String DATA_FOLDER 			= "/group/gc83/c83014/hdfs-mpi-inmemory";
	
	/**
	 * Java home path. "/usr/local/java/openjdk7" is JAVA_HOME on FX10.
	 */
	public static String JAVA_HOME				= "/usr/local/java/openjdk7";

	/**
	 * Username on FX10
	 */
	public static String USERNAME					= "c83014";
	
	/**
	 * Apps
	 */
	public static String MAPREDUCE_JOB 	= DEPLOY_FOLDER + "/apps.sh";

	/**
	 * Log
	 */
	public static String CPU_LOG 		= DEPLOY_FOLDER + "/log/cpu_log_";	
	public static String ANALYSIS_LOG 	= DEPLOY_FOLDER + "/log/";		
	
	public static String SHUFFLE_ENGINE_LOG = DEPLOY_FOLDER + "/log/shuffle_engine_";
	
	/**
	 * Running time for Hadoop cluster
	 */
	public static String ELAPSED_TIME				= "00:30:00";	
	
	public static int NUMBER_PROCESS_EACH_NODE 	= 4;
	
	public static int NUMBER_OF_EXTRA_NODE = 2;
	
	public static int MAXIMUM_DIRECT_MEMORY = 0*1024*1024;
	
	/**
	 * Lock file
	 */
	public static String LOCK_FILE_PATH = DEPLOY_FOLDER + "/hadoop/lock/";
	
	public static void setTsubame(){
		HADOOP_TAR_GZ_FILE 		= "/work1/t2g-16IAI/chung/hadoop-mpi-inmemory/deploy/hadoop.tar.gz";			
		DEPLOY_FOLDER 			= "/work1/t2g-16IAI/chung/hadoop-mpi-inmemory/deploy";	
		DATA_FOLDER				= "/work1/t2g-16IAI/chung/hdfs-mpi-inmemory";
		JAVA_HOME				= "/home/usr4/16IAI819/.local/jdk1.7.0_65";	
		USERNAME				= "16IAI819";		
		
		MAPREDUCE_JOB 			= DEPLOY_FOLDER + "/apps.sh";
		
		CPU_LOG 				= DEPLOY_FOLDER + "/log/cpu_log_";	
		ANALYSIS_LOG 			= DEPLOY_FOLDER + "/log/";		
		
		ELAPSED_TIME			= "00:30:00";	
		
		NUMBER_PROCESS_EACH_NODE= 7;	
		NUMBER_OF_EXTRA_NODE = 10;
		MAXIMUM_DIRECT_MEMORY = 0*1024*1024;
		
		LOCK_FILE_PATH = DEPLOY_FOLDER + "/hadoop/lock/";		
	}
	
	public static void setFX10(){
	}	
}
