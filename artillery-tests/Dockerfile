# Use a Node.js base image
FROM node:latest

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Install Artillery globally
RUN npm install -g artillery

# Copy the rest of the code
COPY . .

# Expose the port that Artillery might use (if needed)
EXPOSE 3000

# Default command when the container starts
CMD ["artillery"]
