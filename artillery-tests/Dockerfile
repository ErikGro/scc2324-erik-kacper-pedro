FROM artilleryio/artillery
WORKDIR /

ADD . /

RUN npm install

RUN npm run generate-test-data

CMD ["run", "/test-all-endpoints.yml"]

