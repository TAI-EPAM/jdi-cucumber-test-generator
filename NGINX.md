# Example nginx configuration for reverse proxy mode
```text
map $remote_addr $proxy_forwarded_elem {
    # IPv4 addresses can be sent as-is
    ~^[0-9.]+$          "for=$remote_addr";
    # IPv6 addresses need to be bracketed and quoted
    ~^[0-9A-Fa-f:.]+$   "for=\"[$remote_addr]\"";
    # Unix domain socket names cannot be represented in RFC 7239 syntax
    default             "for=unknown";
}

server {
  server_name bddgenerator.example.com;
  location / {
    # RFC 7239
    # This header contains information erased by nginx in reverse proxy mode
    proxy_set_header Forwarded "$proxy_forwarded_elem;proto=$scheme;host=$host";
    proxy_pass http://127.0.0.1:8080/;
  }
}
```