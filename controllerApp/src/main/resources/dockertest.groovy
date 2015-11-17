import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.core.DockerClientBuilder
import com.github.dockerjava.core.DockerClientConfig

/**
 * Created by grant on 10/20/15.
 */
DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
    .withVersion("1.16")
    .withUri("https://my-docker-host.tld:2376")
    .withUsername("dockeruser")
    .withPassword("ilovedocker")
    .withEmail("dockeruser@github.com")
    .withServerAddress("https://index.docker.io/v1/")
    .withDockerCertPath("/home/user/.docker")
    .build();
DockerClient docker = DockerClientBuilder.getInstance(config).build();

