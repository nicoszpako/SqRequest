Adds following expressions:

### HTTP request result using POST method
Returns the result of a POST method sent to the given address with the given parameters.\
*ex*: ```result of http post "sqript.fr" with values (dictionary with [["username\","nico-"],["password",314159268]]```\
pattern: \[result of] http post \[to] {string} \[with values {nbttagcompound|dictionary|string}]
### HTTP request result using GET method
Returns the result of a GET method sent to the given address with the given headers.\
*ex*: ```result of http post "sqript.fr" with headers (dictionary with [["username","nico-"],["password",314159268]]```\
*pattern*: \[result of] http get \[from] {string} \[with header\[s] {dictionary}]
