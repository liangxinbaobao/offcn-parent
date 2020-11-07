#基础镜像
FROM jdk1.8
MAINTAINER ujiuye
#ADD 上传jar包
ADD scw-register-*.jar /app.jar
#配置环境变量，用于接收外部参数
#启动端口
ENV PORT=""
#注册中心的地址
ENV EUREKA=""
EXPOSE 8888
ENTRYPOINT ["sh","-c","java -jar $PORT $EUREKA app.jar"]
