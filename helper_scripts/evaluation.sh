#!/bin/bash

# script for using https://github.com/usnistgov/trec_eval to evaluate files in ../out directory
# e.g. ./evaluation.sh -q ../../trec_eval/new.qrels -r ../out -p out-peyrer_BM25

while getopts q:r:p: option
do
case "${option}"
in
q) QREL_FILE=${OPTARG};;
r) RESULT_DIR=${OPTARG};;
p) RESULT_FILE_PREFIX=${OPTARG};;
esac
done
for f in "${RESULT_DIR}/${RESULT_FILE_PREFIX}"*.trec; do
  echo "${f}"
     ../../trec_eval/trec_eval -m ndcg_cut.5,10 "${QREL_FILE}" "${f}"
  echo ""
done
