<?xml version="1.0"?>
<!--
Gregory Buchenberger
Copyright (C) 2009 Eightfold Consulting LLC

 This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with this program;
 if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:fox="http://xmlgraphics.apache.org/fop/extensions"
                xmlns:dtb="http://www.daisy.org/z3986/2005/dtbook/"
                exclude-result-prefixes="dtb">
                
  <!--images-->
  <xsl:template match="dtb:img">
    <fo:block xsl:use-attribute-sets="spacing">
      <fo:external-graphic src="{@src}"
                           id="{@id}"
                           fox:alt-text="{@alt}"
                           content-height="100%"
                           content-width="100%"
                           scaling="uniform" />
    </fo:block>
  </xsl:template>
  
  <!--image groups-->
  <xsl:template match="dtb:imggroup">
    <fo:block xsl:use-attribute-sets="spacing">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>

</xsl:stylesheet>
