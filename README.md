# RouterBase Java Client

[RouterBase](https://routerbase.com) provides an OpenAI-compatible API at `https://routerbase.com/v1`. This package is a Maven-ready Java client for chat completions and model listing.

## Install

```xml
<dependency>
  <groupId>com.routerbase</groupId>
  <artifactId>routerbase-client</artifactId>
  <version>0.1.1</version>
</dependency>
```

## Usage

```java
import com.routerbase.ChatMessage;
import com.routerbase.RouterBaseClient;

var client = new RouterBaseClient(System.getenv("ROUTERBASE_API_KEY"));
var response = client.chatCompletion(List.of(
    new ChatMessage("user", "Explain RouterBase in one sentence.")
));

System.out.println(response.getChoices().get(0).getMessage().getContent());
```

## Links

- [RouterBase](https://routerbase.com)
- [RouterBase docs](https://docs.routerbase.com/)
- [Chat completions docs](https://docs.routerbase.com/api-reference/chat-completions)

## License

MIT
