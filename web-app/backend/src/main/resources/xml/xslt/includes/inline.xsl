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
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:fox="http://xmlgraphics.apache.org/fop/extensions"
                xmlns:dtb="http://www.daisy.org/z3986/2005/dtbook/"
                exclude-result-prefixes="dtb">


  <!--word-->
  <xsl:template match="dtb:w">
    <fo:inline font-weight="bold">
      <xsl:apply-templates />
    </fo:inline>
  </xsl:template>
  
  <!--linenum-->
  <xsl:template match="dtb:linenum">
    <fo:inline font-weight="bold">
      <xsl:apply-templates/>
    </fo:inline>
  </xsl:template>
  
  <!--cite-->
  <xsl:template match="dtb:cite">
    <xsl:choose>
      <xsl:when test="parent::em">
        <fo:inline font-style="normal">
          <xsl:apply-templates/>
        </fo:inline>
      </xsl:when>
      <xsl:otherwise>
        <fo:inline font-style="italic">
          <xsl:apply-templates/>
        </fo:inline>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <!--subscript-->
  <xsl:template match="dtb:sub">
    <fo:inline xsl:use-attribute-sets="sub">
      <xsl:apply-templates />
    </fo:inline>
  </xsl:template>
  <!--superscript-->
  <xsl:template match="dtb:sup">
    <fo:inline xsl:use-attribute-sets="sup">
      <xsl:apply-templates />
    </fo:inline>
  </xsl:template>

  <!--note-->
  <xsl:template match="dtb:note">
    <fo:inline id="{@id}">
      <xsl:apply-templates/>
    </fo:inline>
  </xsl:template>

  <!--noteref-->
  <xsl:template match="dtb:noteref">
    <fo:inline>
      <xsl:choose>
        <xsl:when test="@idref">
          <xsl:choose>
            <!-- use superscript for single character noteref -->
            <xsl:when test="string-length(text()) &lt; 2">
              <fo:basic-link xsl:use-attribute-sets="sup"
                internal-destination="{@idref}">
                <xsl:apply-templates/>
              </fo:basic-link>
            </xsl:when>
            <xsl:otherwise>
              <fo:basic-link
                internal-destination="{@idref}">
                <xsl:apply-templates/>
              </fo:basic-link>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates/>
        </xsl:otherwise>
      </xsl:choose>
    </fo:inline>
  </xsl:template>

  <!--anchors (links)-->
  <xsl:template match="dtb:a">
    <xsl:choose>
      <xsl:when test="@name">
        <xsl:if test="not(name(following-sibling::*[1]) = 'h1')">
          <fo:block xsl:use-attribute-sets="spacing"
                    id="{@name}" />
        </xsl:if>
      </xsl:when>
      <xsl:when test="@href">
        <fo:basic-link>
          <xsl:choose>
            <xsl:when test="starts-with(@href, '#') and not(@external = 'true')">
              <xsl:attribute name="internal-destination">
                <xsl:value-of select="substring(@href, 2)" />
              </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
              <xsl:attribute name="external-destination">
                <xsl:value-of select="@href" />
              </xsl:attribute>
            </xsl:otherwise>
          </xsl:choose>
          <xsl:apply-templates/>
        </fo:basic-link>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!--italic-->
  <xsl:template match="dtb:em">
    <fo:inline font-style="italic">
      <xsl:apply-templates/>
    </fo:inline>
  </xsl:template>
  <!--bold-->
  <xsl:template match="dtb:strong">
    <fo:inline font-weight="bold">
      <xsl:apply-templates/>
    </fo:inline>
  </xsl:template>
</xsl:stylesheet>
