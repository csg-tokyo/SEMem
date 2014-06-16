/*
 * Main.c
 *
 *  Created on: Feb 11, 2014
 *      Author: chung
 */

#include "mrhpc.h"

// Example of PageRank computation

/* Mapper class */
class PR_MAP: public Mapper {
public:
	virtual void Map(const string &key, const string &value){
		vector<std::string> item = LIB::split(value, ',');
		if (item.size() >= 2) {
			string comment = item[item.size() - 2];
			if (comment.length() > 12) {
				comment = comment.substr(11, comment.length() - 12);

				// Split words
				vector<std::string> words = LIB::split(comment, '\\');
				for (int i = 0; i < words.size(); i++) {
					if (words[i].length() > 0 && words[i][0] == 'u') {
						// Emit word
						Emit(words[i], "1");
					}
				}
			}
		}
	}
};

/* Reducer class */
class PR_REDUCE: public Reducer{
public:
	virtual void Reduce(const string &key, vector<string> value){
		// Print only
		cout << key << ": ";
		cout << value.size() << "\n";
	}
};

int main(int argc, char *argv[]) {
	// Run JOB on COMM_WORLD communicator
	MPI::Init(argc, argv);

	// Set number of mapper and reducer
	MR_JOB pr (72, 72);

	// Set mapper and reducer functions
	PR_MAP map;
	pr.setM_Task(map);
	PR_REDUCE reduce;
	pr.setR_Task(reduce);

	// Set input data
	pr.setInputDir("./data");
	pr.setInputFormat("*.dat");

	// Set temporary path
	pr.setTmpDir("./tmp");

	// Start job
	pr.startJob();

	MPI::Finalize();
	return 0;
}
