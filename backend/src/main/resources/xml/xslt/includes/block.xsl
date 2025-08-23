<?xml version="1.0" encoding="utf-8"?>
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
  
  <!--definition list-->
  <xsl:template match="dtb:dl">
    <fo:block xsl:use-attribute-sets="spacing">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  
  <!--term in a definition list-->
  <xsl:template match="dtb:dt">
    <fo:block font-weight="bold" keep-with-next="5">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  
  <!--definition-->
  <xsl:template match="dtb:dd">
    <fo:block start-indent=".5in">
      <xsl:attribute name="space-after">
        <!--adjusts space-after based on end of list-->
        <xsl:choose>
          <xsl:when test="name(following::*[1]) = 'dd'">
            <xsl:text>0.5em</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>1em</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:attribute>
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  
  <!--author-->
  <xsl:template match="dtb:author">
    <fo:block>
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>

  <!-- title -->
  <xsl:template match="dtb:title">
    <fo:block>
      <xsl:call-template name="h3"/>
    </fo:block>
  </xsl:template>
  
  <!--paragraph-->
  <xsl:template name="p" match="dtb:p">
    <fo:block xsl:use-attribute-sets="spacing">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>

  <!--code-->
  <xsl:template match="dtb:code">
    <!-- Test to see if there is surronding text and style as inline.
    otherwise style as block -->
    <xsl:choose>
      <xsl:when test="parent::node()/text()[normalize-space(.)]">
        <fo:inline font-family="monospace">
          <xsl:apply-templates/>
        </fo:inline>
      </xsl:when>
      <xsl:otherwise>
        <fo:block xsl:use-attribute-sets="spacing" font-family="monospace" linefeed-treatment="preserve">
          <xsl:apply-templates/>
        </fo:block>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- poem -->
  <xsl:template match="dtb:poem">
    <xsl:call-template name="p"/>
  </xsl:template>
  
  <xsl:template match="dtb:linegroup">
    <xsl:call-template name="p"/>
  </xsl:template>

  <!-- breaks -->
  <xsl:template match="dtb:br|dtb:line|dtb:dateline">
    <fo:block>
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  
  <xsl:template match="dtb:byline">
    <fo:block font-style="italic" keep-with-previous="5">
      <xsl:call-template name="p"/>
    </fo:block>
  </xsl:template>
  
  <xsl:template match="dtb:epigraph">
    <fo:block font-style="italic" keep-with-next="5">
      <xsl:call-template name="p"/>
    </fo:block>
  </xsl:template>

  <!--header-->
  <xsl:template match="dtb:hd">
    <!--deal with headers within list by enclosing in list-item-->
    <xsl:choose>
      <xsl:when test="parent::dtb:list">
        <fo:list-item xsl:use-attribute-sets="spacing">
          <fo:list-item-label end-indent="label-end()">
            <fo:block/>
          </fo:list-item-label>
          <fo:list-item-body start-indent="body-start()">
            <xsl:call-template name="h3"/>
          </fo:list-item-body>
        </fo:list-item>
      </xsl:when>
      <!--default behavior-->
      <xsl:otherwise>
        <xsl:call-template name="h3"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <!--producers note (render only if required)-->
  <xsl:template match="dtb:prodnote">
    <xsl:choose>
      <xsl:when test="@render='required'">
        <xsl:choose>
          <xsl:when test="parent::dtb:imggroup">
            <fo:block keep-with-previous.within-page="3">
              <fo:inline font-weight="bold">
                <xsl:apply-templates/>
              </fo:inline>
            </fo:block>
          </xsl:when>
          <xsl:when test="parent::dtb:list">
            <fo:list-item space-after="1.25em">
              <fo:list-item-label end-indent="label-end()">
                <fo:block/>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block>
                  <fo:inline font-weight="bold">
                    <xsl:apply-templates/>
                  </fo:inline>
                </fo:block>
              </fo:list-item-body>
            </fo:list-item>
          </xsl:when>
          <xsl:otherwise>
            <fo:block keep-with-previous.within-page="3">
              <fo:inline font-weight="bold">
                <xsl:apply-templates/>
              </fo:inline>
            </fo:block>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:otherwise>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!--sidebar-->
  <xsl:template match="dtb:sidebar">
    <fo:block-container xsl:use-attribute-sets="spacing" id="{@id}" border-left-style="double" border-left-width="10px"
      border-left-color="black" start-indent=".2in">
      <fo:block keep-together="5">
        <xsl:apply-templates/>
      </fo:block>
    </fo:block-container>
  </xsl:template>

  <!--blockquote-->
  <xsl:template match="dtb:blockquote">
    <fo:block xsl:use-attribute-sets="spacing" start-indent="0.5in" end-indent="0.5in"
      text-align="start">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>

  <!--page numbers-->
  <xsl:template match="dtb:pagenum">
    <xsl:choose>
      <!--deal with pagenum within list by enclosing in list-item-->
      <xsl:when test="parent::dtb:list">
        <fo:list-item>
          <fo:list-item-label end-indent="label-end()">
            <fo:block/>
          </fo:list-item-label>
          <fo:list-item-body start-indent="body-start()">
            <fo:block xsl:use-attribute-sets="inline_page_numbers">
              
              <!--marker used in header and footer-->
              <fo:marker marker-class-name="pn">
                <xsl:value-of select="*|text()"/>
              </fo:marker>
              Begin: <xsl:value-of select="*|text()"/>
            </fo:block>
          </fo:list-item-body>
        </fo:list-item>
      </xsl:when>
      <xsl:otherwise>
        <!--default behavior-->
        <fo:block xsl:use-attribute-sets="inline_page_numbers">

          <!--marker used in header and footer-->
          <fo:marker marker-class-name="pn">
            <xsl:value-of select="*|text()"/>
          </fo:marker>
          Begin: <xsl:value-of select="*|text()"/>
        </fo:block>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>
