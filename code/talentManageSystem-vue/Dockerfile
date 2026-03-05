# 使用 Node.js 20.11.0 镜像构建前端应用
FROM node:20.11.0 AS build

# 设置工作目录
WORKDIR /opt/talent-management-system-vue

# 将当前目录复制到容器中的工作目录
COPY . .

# 安装依赖并构建项目
RUN npm install && npm run build

# 使用 Nginx 镜像来提供构建后的静态文件
FROM nginx:alpine

# 将构建后的静态文件复制到 Nginx 服务器的 html 目录
COPY --from=build /opt/talent-management-system-vue/dist /usr/share/nginx/html

# 暴露 Nginx 服务的端口
EXPOSE 80
