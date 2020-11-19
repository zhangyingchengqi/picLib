启动后, 服务监控地址:
  http://localhost:9001/hystrix
  
在要监控的服务上加入相关配置并打开监控端点后， 通过以下访问:
    http://admin:a@localhost:8888/actuator/hystrix.stream