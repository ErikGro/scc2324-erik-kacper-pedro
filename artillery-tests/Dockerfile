FROM artilleryio/artillery
WORKDIR /
# Add everything in the current directory to our image, in the working directory '/'
ADD . /


RUN npm install

# Run generate-test-data using npm
RUN npm run generate-test-data

# Run the test using npm
CMD ["run", "/test-all-endpoints.yml"]

# # Expose the port that Artillery might use (if needed)
# EXPOSE 3000