FROM mongo:4.2

COPY ./ /

RUN chmod +x ./mongoseed-entrypoint.dev.sh
ENTRYPOINT ["./mongoseed-entrypoint.dev.sh"]