package org.miaohong.fishrpc.register.discovery;

public class ServiceInstance<T> {

    private final String name;
    private final String id;
    private final String address;
    private final Integer port;
    private final Integer sslPort;
    private final T payload;
    private final long registrationTimeUTC;
    private final ServiceType serviceType;
    private final boolean enabled;

    public ServiceInstance(String name, String id, String address, Integer port, Integer sslPort, T payload, long registrationTimeUTC, ServiceType serviceType, boolean enabled) {
        this.name = name;
        this.id = id;
        this.serviceType = serviceType;
        this.address = address;
        this.port = port;
        this.sslPort = sslPort;
        this.payload = payload;
        this.registrationTimeUTC = registrationTimeUTC;
        this.enabled = enabled;
    }
}
