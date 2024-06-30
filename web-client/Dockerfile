# Etapa de construcción (build)
FROM node:19.9.0 as build

ENV NODE_OPTIONS="--openssl-legacy-provider"

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build --prod

FROM nginx:1.21.1-alpine

COPY --from=build /app/dist/angular10-springboot-client /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
