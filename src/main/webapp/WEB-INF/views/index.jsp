<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
  <div class="form">
    <form action="index" method="post">
      <table>
        <tr>
          <td>Input valid url to index pages</td>
          <td><input id="q" name="q"></td>
        </tr>
         <tr>
          <td>Input total depth for parsing</td>
          <td><input id="d" name="d" value="3"></td>
        </tr>
        <tr>
          <td><input type="submit" value="Index"></td>
        </tr>
      </table>
    </form>
  </div>

</body>
</html>