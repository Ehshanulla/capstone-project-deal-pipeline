# Build stage
FROM node:20-alpine AS build

WORKDIR /app
COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build -- --configuration production

# Runtime stage
FROM nginx:alpine

# REMOVE default config
RUN rm /etc/nginx/conf.d/default.conf

# Copy custom nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# IMPORTANT: copy ONLY the browser folder
COPY --from=build /app/dist/InvestmentBankingDealPipelineManagementPortalFrontEnd/browser /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
