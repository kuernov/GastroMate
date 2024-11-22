# Use Node.js base image
FROM node:18

# Set working directory
WORKDIR /app

# Copy package.json and install dependencies
COPY package*.json ./
RUN npm install

# Copy source code
COPY . .

# Set environment variable to enable hot reloading
ENV CHOKIDAR_USEPOLLING=true

# Expose port 3000 to access the dev server
EXPOSE 3000

# Start the React app in development mode
CMD ["npm", "start"]
