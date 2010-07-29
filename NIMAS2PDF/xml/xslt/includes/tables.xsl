<?xml version="1.0" encoding="UTF-8"?>
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
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fo="http://www.w3.org/1999/XSL/Format"
  xmlns:fox="http://xmlgraphics.apache.org/fop/extensions"
  xmlns:dtb="http://www.daisy.org/z3986/2005/dtbook/" exclude-result-prefixes="dtb">
  <!--table-->
  <xsl:template match="dtb:table">
    <fo:table xsl:use-attribute-sets="spacing" id="{generate-id(.)}" table-layout="fixed" font-size="{$tableFontSize}" inline-progression-dimension="100%"
      border-collapse="collapse" width="100%" border="solid 2px black" keep-together="3">
      <xsl:choose>
        <xsl:when test="dtb:tr/dtb:th">
          <fo:table-header>
            <xsl:for-each select="dtb:tr/dtb:th/parent::*">
              <xsl:call-template name="tr"/>
            </xsl:for-each>
          </fo:table-header>
        </xsl:when>
        <xsl:when test="dtb:thead">
          <xsl:for-each select="dtb:thead">
            <fo:table-header>
              <xsl:for-each select="dtb:tr">
                <xsl:call-template name="tr"/>
              </xsl:for-each>
            </fo:table-header>
          </xsl:for-each>
        </xsl:when>
      </xsl:choose>

      <xsl:choose>
        <xsl:when test="dtb:tr/dtb:td">
          <fo:table-body>
            <xsl:for-each select="dtb:tr/dtb:td/parent::*">
              <xsl:call-template name="tr"/>
            </xsl:for-each>
          </fo:table-body>
        </xsl:when>
        <xsl:when test="dtb:tbody">
          <xsl:for-each select="dtb:tbody">
            <fo:table-body>
              <xsl:for-each select="dtb:tr">
                <xsl:call-template name="tr"/>
              </xsl:for-each>
            </fo:table-body>
          </xsl:for-each>
        </xsl:when>
      </xsl:choose>
    </fo:table>
  </xsl:template>

  <xsl:template name="thead">
    <fo:table-header>
      <xsl:apply-templates/>
    </fo:table-header>
  </xsl:template>

  <xsl:template name="tr">
    <fo:table-row keep-together="5">
      <xsl:apply-templates/>
    </fo:table-row>
  </xsl:template>

  <xsl:template match="dtb:th">
    <fo:table-cell border="solid 2px black" padding="4px" text-align="center" font-weight="bold">
      <xsl:if test="@colspan and @colspan != ''">
        <xsl:attribute name="number-columns-spanned">
          <xsl:value-of select="@colspan"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="@rowspan and @rowspan != ''">
        <xsl:attribute name="number-rows-spanned">
          <xsl:value-of select="@rowspan"/>
        </xsl:attribute>
      </xsl:if>
      <fo:block>
        <xsl:choose>
          <xsl:when test="not(normalize-space(.))">
            <xsl:text>&#160;</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:apply-templates/>
          </xsl:otherwise>
        </xsl:choose>
      </fo:block>
    </fo:table-cell>
  </xsl:template>


  <xsl:template match="dtb:td">
    <fo:table-cell border="solid 1px black" padding="4px" >
      <xsl:if test="@colspan and @colspan != ''">
        <xsl:attribute name="number-columns-spanned">
          <xsl:value-of select="@colspan"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="@rowspan and @rowspan != ''">
        <xsl:attribute name="number-rows-spanned">
          <xsl:value-of select="@rowspan"/>
        </xsl:attribute>
      </xsl:if>
      <fo:block>
        <xsl:choose>
        <xsl:when test="not(normalize-space(.))">
          <xsl:text>&#160;</xsl:text>
		  <xsl:apply-templates/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates/>
        </xsl:otherwise>
      </xsl:choose>
      </fo:block>
    </fo:table-cell>
  </xsl:template>
</xsl:stylesheet>
