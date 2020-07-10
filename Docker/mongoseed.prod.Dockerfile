FROM mongo:4.2

COPY ./ /

RUN chmod +x ./mongoseed-entrypoint.prod.sh
ENTRYPOINT ["./mongoseed-entrypoint.prod.sh"]