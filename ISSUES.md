###Jira issues
Jira functionality is not active. Application fails to execute any operation.
Sending REST call on EPAM jira server gives response with an error 500.

#####For example:
**Request:** 
*curl -D- -u epam_employee@epam.com:topsecret -X GET -H "Content-Type: application/json" https://jirapct.epam.com/jira/rest/api/2/filter/1 > response.txt*\
**Response:** 
*java.lang.IllegalStateException: Cannot call sendError() after the response has been committed*
