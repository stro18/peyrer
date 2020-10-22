#!/bin/bash

# script for evaluating output files by means of https://github.com/usnistgov/trec_eval with data from
# https://git.webis.de/code-teaching/readings/information-retrieval-ss20-leipzig/-/tree/master/Evaluation%20Data
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
