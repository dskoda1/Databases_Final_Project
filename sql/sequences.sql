/*
File for the sequences we need. pur#_seq starts with 100015 but should really
be starting with 100000. Would need to edit insertBasicTuples to use the sequence
if we want to start at 100000.

*/
CREATE SEQUENCE pur#_seq
	START WITH 100015
	INCREMENT BY 1;

CREATE SEQUENCE sup#_seq
	start with 1000
	increment by 1;
