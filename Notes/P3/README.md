###1. Tomcat Configuration
* **server.tomcat.accept-count** :  Default100. The maximum queue length for incoming connection requests when all possible request processing threads are in use. Any requests received when the queue is full will be refused. The default value is 100.
* **server.tomcat.max-connections** : Default10000. The maximum number of connections that the server will accept and process at any given time. When this number has been reached, the server will accept, but not process, one further connection.
* **server.tomcat.max-threads** : 200.The maximum number of request processing threads to be created by this Connector, which therefore determines the maximum number of simultaneous requests that can be handled. If not specified, this attribute is set to 200. If an executor is associated with this connector, this attribute is ignored as the connector will execute tasks using the executor rather than an internal thread pool.
* (理解：max-threads/服务程序可以同时处理的线程数,可以理解为通过设定 maxConnections=10 ，同时可以建立10个连接，maxThreads=3，则这10个连接中同时只有3个连接被处理，其余7个连接都在queue-2中等待被处理，等这3个连接处理完之后，其余的7个连接中的3个才可以被处理。如果处理完的3个连接关闭后，queue-1中就可以有3个连接进入queue-2)
* **server.tomcat.min-spare-threads** : Default 10 最小工作线程数（应急）

###2. JMeter: Testing tools to test our system
* Under the default configuration, the maximum concurrent requests is 200+100. When the clients' requests exceed this number, the server will rejects the request.
* Compare max-connections with max-threads: (maxConnections 默认值是 10000,而最大并发默认200,NIO也就是异步IO,处理完请求后就把客户端挂起,继续处理下一个任务,等到客户端有新的消息再处理,最多能挂10000个,在jmeter设置的keepalive就是为了发送请求后不断开连接来模拟真实环境,而服务器为了能接收更多的请求,我们需要把keepalive设置时间,超过30s就服务器给你断开连接 ,把最大连接数理解为餐厅的座位,吃完了你可以坐那里不走 ,最大并发数就是厨师,客人要点菜的时候才做,上菜之后就不管了,而等待数就是排队号,超过了就不让等了)

###3. Best performance for Quad-core 8G CPU

* max-threads : 800 (when this number is too large, the os will spend to much time in switching thread)
* accept-count :1000
* min-spare-threads :100

###4. Customized Configurable Tomcat in server
* **keepAlive** : （HTTP长连接，复用链接）The Keep-Alive general header allows the sender to hint about how the connection may be used to set a timeout and a maximum amount of requests.
* **keepAliveTimeOut** : 多少毫秒若客户端不响应的断开keepalive。set to 30s.
* **maxKeepAliveRequests** :  多少次请求后keepalive断开失效。set to 10000.
