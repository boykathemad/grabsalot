
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
	<xsl:output method="html" encoding="utf-8" indent="yes"/>
	<xsl:template match="/">
		<html>
			<body>
				<h1>Recent tracks</h1>
				<table width="100%">
					<tr>
						<th></th>
						<th>Date</th>
						<th>Artist</th>
						<th>Album</th>
						<th>Track title</th>
					</tr>
					<xsl:for-each select="//track">
						<tr>
							<td align="center">
								<xsl:if test="image[@size='small'] != ''">
								<img>
									<xsl:attribute name="src">
										<xsl:value-of select="image[@size='small']" />
									</xsl:attribute>
								</img>
								</xsl:if>
							</td>
							<td>
								<xsl:value-of select="date"/>
							</td>
							<td>
								<xsl:value-of select="artist"/>
							</td>
							<td>
								<xsl:value-of select="album"/>
							</td>
							<td>
								<xsl:value-of select="name"/>
							</td>
						</tr>
					</xsl:for-each>
				</table>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>
