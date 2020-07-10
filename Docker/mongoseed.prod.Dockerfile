FROM mongo:4.2

COPY ./ /

RUN chmod +x ./mongoseed-entrypoint.sh
ENTRYPOINT ["./mongoseed-entrypoint.sh"]