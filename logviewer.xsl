<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
  <head>
    <style type="text/css">
    <![CDATA[
    td {
        font-weight: bold;
    }
    .level_WARNING {
        background-color:orange;
    }
    .level_SEVERE {
        background-color:red;
    }
    .level_FINE, .level_FINER, .level_FINEST {
        background-color:green;
    }
    
    ]]>
    </style>
  </head>
  <body>
  <h2>Java log viewer</h2>
    <table border="1" width="100%">
      <tr bgcolor="#9acd32">
        <th>#</th>
        <th>Level</th>
        <th>Date</th>
        <th>Message</th>
        <th>Class</th>
        <th>Method</th>
      </tr>
      <xsl:for-each select="log/record">
      <!-- POSSIBLE LEVEL VALUES :
          SEVERE (highest value)
    WARNING
    INFO
    CONFIG
    FINE
    FINER
    FINEST (lowest value) 
    -->
      <tr class="level_{level}">
        <td><xsl:value-of select="sequence"/></td>
        <td><xsl:value-of select="level"/></td>
        <td><xsl:value-of select="date"/></td>
        <td><xsl:value-of select="message"/></td>
        <td><xsl:value-of select="class"/></td>
        <td><xsl:value-of select="method"/></td>
      </tr>
      </xsl:for-each>
    </table>
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>