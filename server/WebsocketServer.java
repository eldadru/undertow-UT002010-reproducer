import static io.undertow.Handlers.path;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainer;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

ServletContainer container = ServletContainer.Factory.newInstance();

WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
webSocketDeploymentInfo.addEndpoint(ServerEndpointConfig.Builder.
	create(ReproducerWebSocketEndpoint.class, "/")
	.configurator(new Configurator())
	.build());

DeploymentInfo builder = new DeploymentInfo()
	.setClassLoader(getClass().getClassLoader())
	.setContextPath("/")
	.addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME, webSocketDeploymentInfo)
	.setDeploymentName("web-socket-reproducer");

DeploymentManager manager = container.addDeployment(builder);
manager.deploy();

HttpHandler handler = manager.start();

Undertow undertowServer = Undertow.builder()
	.addHttpListener(80, "127.0.0.1")
	.setHandler(path().addPrefixPath("/", handler))
	.build();

undertowServer.start();
Thread.sleep(Long.MAX_VALUE);