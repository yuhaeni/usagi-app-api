FROM nginx:alpine

ARG ENVIRONMENT=dev

COPY nginx/nginx.${ENVIRONMENT}.conf /etc/nginx/nginx.conf
COPY nginx/.htpasswd /etc/nginx/.htpasswd

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
