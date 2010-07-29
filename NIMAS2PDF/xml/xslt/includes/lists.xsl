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



  <!--list-->
  <xsl:template match="dtb:list">

    <fo:list-block xsl:use-attribute-sets="list-geometry">
      <!--tests to see if the current list is within another list and adjusts the space-after-->
      <xsl:attribute name="space-after">
        <xsl:choose>
          <xsl:when test="parent::dtb:list">
            <xsl:text>0.5em</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>1em</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:attribute>
      <!--counts ancestor list instances and uses count to adjust start-indent-->
      <xsl:attribute name="start-indent">
        <xsl:variable name="ancestors">
          <xsl:choose>
            <xsl:when test="count(ancestor::dtb:list)">
              <xsl:value-of select="1 + count(ancestor::dtb:list) * 1.25"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:text>1</xsl:text>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:value-of select="concat($ancestors, 'cm')"/>
      </xsl:attribute>
      <xsl:apply-templates select="*"/>
    </fo:list-block>
  </xsl:template>

  <!--list item-->
  <xsl:template match="dtb:li">
    <xsl:choose>
      <xsl:when test="../@type = 'ol' and ../@enum">
        <!--ordered list-->
        <fo:list-item xsl:use-attribute-sets="list-item-geometry">
          <fo:list-item-label xsl:use-attribute-sets="list-item-label-geometry">
            <fo:block>
              <xsl:variable name="value-attr">
                <xsl:choose>
                  <xsl:when test="../@start">
                    <xsl:number value="position() + ../@start - 1"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:number value="position()"/>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:variable>
              <xsl:choose>
                <xsl:when test="../@enum='i'">
                  <xsl:number value="$value-attr" format="i."/>
                </xsl:when>
                <xsl:when test="../@enum='I'">
                  <xsl:number value="$value-attr" format="I."/>
                </xsl:when>
                <xsl:when test="../@enum='a'">
                  <xsl:number value="$value-attr" format="a."/>
                </xsl:when>
                <xsl:when test="../@enum='A'">
                  <xsl:number value="$value-attr" format="A."/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:number value="$value-attr" format="1."/>
                </xsl:otherwise>
              </xsl:choose>
            </fo:block>
          </fo:list-item-label>
          <fo:list-item-body xsl:use-attribute-sets="list-item-body-geometry">
            <fo:block>
              <xsl:apply-templates select="*|text()"/>
            </fo:block>
          </fo:list-item-body>
        </fo:list-item>
      </xsl:when>
      <xsl:otherwise>
        <fo:list-item xsl:use-attribute-sets="list-item-geometry">
          <fo:list-item-label xsl:use-attribute-sets="list-item-label-geometry">
            <fo:block/>
          </fo:list-item-label>
          <fo:list-item-body xsl:use-attribute-sets="list-item-body-geometry">
            <fo:block>
              <xsl:apply-templates select="*|text()"/>
            </fo:block>
          </fo:list-item-body>
        </fo:list-item>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <!--list item content-->
  <xsl:template match="dtb:lic">
    <fo:inline margin-left="1em">
      <xsl:apply-templates select="*|text()"/>
    </fo:inline>
  </xsl:template>
</xsl:stylesheet>
