FROM nginx:alpine

ARG ENVIRONMENT=dev

COPY nginx/nginx.${ENVIRONMENT}.conf /etc/nginx/nginx.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]