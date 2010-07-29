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
  <!--book-->
  <xsl:template match="dtb:book">
    <fo:block>
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <!--doctitle for cover page-->
  <xsl:template match="//dtb:frontmatter/dtb:doctitle">
    <fo:block xsl:use-attribute-sets="spacing" font-size="200%" font-family="{$headerFontFamily}"
      line-height="{$lineHeight}" font-weight="bold" keep-together="3" break-after="page">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <!--docauthor for frontmatter-->
  <xsl:template match="//dtb:frontmatter/dtb:docauthor">
    <fo:block xsl:use-attribute-sets="spacing" font-family="{$headerFontFamily}"
      font-weight="bold">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <!--level1-->
  <xsl:template match="dtb:level1">
    <fo:block id="{generate-id(.)}">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <!--level2-->
  <xsl:template match="dtb:level2">
    <fo:block id="{generate-id(.)}">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <!--level3-->
  <xsl:template match="dtb:level3">
    <fo:block id="{generate-id(.)}">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <!--level4-->
  <xsl:template match="dtb:level4">
    <fo:block id="{generate-id(.)}">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <!--level5-->
  <xsl:template match="dtb:level5">
    <fo:block id="{generate-id(.)}">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <!--level6-->
  <xsl:template match="dtb:level6">
    <fo:block id="{generate-id(.)}">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <!--frontmatter-->
  <xsl:template match="dtb:frontmatter">
    <fo:block break-after="page">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <!--bodymatter-->
  <xsl:template match="dtb:bodymatter">
    <fo:block break-after="page">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <!--rearmatter-->
  <xsl:template match="dtb:rearmatter">
    <fo:block>
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <!--headers-->
  <xsl:template name="h1" match="dtb:h1">
    <fo:block xsl:use-attribute-sets="spacing" id="{generate-id(.)}" font-size="2em" font-family="{$headerFontFamily}"
      line-height="{$lineHeight}" font-weight="bold">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <xsl:template name="h2" match="dtb:h2">
    <fo:block xsl:use-attribute-sets="spacing" id="{generate-id(.)}" line-height="{$lineHeight}" font-size="1.75em"
      font-family="{$headerFontFamily}" font-weight="bold">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <xsl:template name="h3" match="dtb:h3">
    <fo:block xsl:use-attribute-sets="spacing" id="{generate-id(.)}" font-size="1.5em" line-height="{$lineHeight}"
      font-family="{$headerFontFamily}" font-weight="bold" keep-with-next="5">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <xsl:template name="h4" match="dtb:h4">
    <fo:block xsl:use-attribute-sets="spacing" id="{generate-id(.)}" font-size="1.25em" line-height="{$lineHeight}"
      font-family="{$headerFontFamily}" keep-with-next="5">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <xsl:template name="h5" match="dtb:h5">
    <fo:block xsl:use-attribute-sets="spacing" id="{generate-id(.)}" font-size="inherit" line-height="{$lineHeight}"
      font-family="{$headerFontFamily}" font-weight="bold" keep-with-next="5">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <xsl:template name="h6" match="dtb:h6">
    <fo:block xsl:use-attribute-sets="spacing" id="{generate-id(.)}" font-size="inherit" line-height="{$lineHeight}"
      font-family="{$headerFontFamily}" font-weight="italic" keep-with-next="5">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
</xsl:stylesheet>
