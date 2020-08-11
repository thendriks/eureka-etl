package nl.maastrichtuniversity.ids.eureka.etl.service;

import nl.maastrichtuniversity.ids.eureka.etl.config.BitStreamConfig;
import nl.maastrichtuniversity.ids.eureka.etl.web.dto.BitStream;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashSet;
import java.util.Set;

@Service
public class DspaceRestClientService {
    private BitStreamConfig bitStreamConfig;

    public DspaceRestClientService(BitStreamConfig bitStreamConfig) {
        this.bitStreamConfig = bitStreamConfig;
    }

    public Set<BitStream> getBitStreams(){
        Set<BitStream> bitstreams = new HashSet<>();
        final WebClient webClient = WebClient.create(bitStreamConfig.getUrl());
        WebClient.RequestBodySpec request = webClient.method(HttpMethod.GET)
                .uri("/core/bitstreams");
        BitStream response = request.exchange().block().bodyToMono(BitStream.class).block();
        return bitstreams;
    }
}
