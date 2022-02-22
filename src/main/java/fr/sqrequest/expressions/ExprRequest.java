package fr.sqrequest.expressions;

import fr.nico.sqript.expressions.ScriptExpression;
import fr.nico.sqript.meta.Feature;
import fr.nico.sqript.meta.Expression;
import fr.nico.sqript.structures.ScriptContext;
import fr.nico.sqript.types.ScriptType;
import fr.nico.sqript.types.TypeDictionary;
import fr.nico.sqript.types.TypeNull;
import fr.nico.sqript.types.primitive.TypeString;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


@Expression(name = "Request Expressions",
        features = {
                @Feature(name = "HTTP request result using POST method", description = "Returns the result of a POST method sent to the given address with the given parameters.", examples = "result of http post \"sqript.fr\" with values (dictionary with [[\"username\",\"nico-\"],[\"password\",314159268]])", pattern = "[result of] http post [to] {string} [with values {nbttagcompound|dictionary|string}]"),
                @Feature(name = "HTTP request result using GET method", description = "Returns the result of a GET method sent to the given address with the given headers.", examples = "result of http post \"sqript.fr\" with headers (dictionary with [[\"username\",\"nico-\"],[\"password\",314159268]])", pattern = "[result of] http get [from] {string} [with header[s] {dictionary}]"),
        }
)
public class ExprRequest extends ScriptExpression {

    @Override
    public ScriptType get(ScriptContext context, ScriptType[] parameters) {
        switch (getMatchedIndex()) {
            case 0:
                TypeString host = (TypeString) parameters[0];
                HttpPost post = new HttpPost(host.getObject());
                List<NameValuePair> urlParameters = new ArrayList<>();

                // add request parameter, form parameters
                ScriptType data = parameters[1];
                if (data != null) {
                    if (data instanceof TypeDictionary) {
                        TypeDictionary dictionary = (TypeDictionary) data;
                        dictionary.getObject().keySet().forEach(k -> urlParameters.add(new BasicNameValuePair(k.toString(), dictionary.getObject().get(k).toString())));
                    } else {
                        try {
                            post.setEntity(new StringEntity(data.toString()));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }

                try {
                    post.setEntity(new UrlEncodedFormEntity(urlParameters));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                try (CloseableHttpClient httpClient = HttpClients.createDefault();
                     CloseableHttpResponse response = httpClient.execute(post)) {
                    return new TypeString(EntityUtils.toString(response.getEntity()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new TypeNull();
            case 1:
                host = (TypeString) parameters[0];
                HttpGet get = new HttpGet(host.getObject());

                // add request parameter, form parameters
                data = parameters[1];
                if (data != null) {
                    if (data instanceof TypeDictionary) {
                        TypeDictionary dictionary = (TypeDictionary) data;
                        dictionary.getObject().keySet().forEach(k -> get.addHeader(k.toString(), dictionary.getObject().get(k).toString()));
                    }
                }

                try (CloseableHttpClient httpClient = HttpClients.createDefault();
                     CloseableHttpResponse response = httpClient.execute(get)) {
                    return new TypeString(EntityUtils.toString(response.getEntity()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new TypeNull();
        }
        return null;
    }

    @Override
    public boolean set(ScriptContext context, ScriptType to, ScriptType[] parameters) {
        return false;
    }
}
