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
<xsl:stylesheet
  exclude-result-prefixes="dtb"
  version="1.0"
  xmlns:dtb="http://www.daisy.org/z3986/2005/dtbook/"
  xmlns:fo="http://www.w3.org/1999/XSL/Format"
  xmlns:fox="http://xmlgraphics.apache.org/fop/extensions"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output
    method="xml" />

  <!--global parameters-->
  <xsl:param
    name="baseFontSize">18pt</xsl:param>
  <xsl:param
    name="tableFontSize">18pt</xsl:param>
  <xsl:param
    name="lineHeight">1.5em</xsl:param>
  <xsl:param
    name="baseFontFamily">DejaVu Sans</xsl:param>
  <xsl:param
    name="headerFontFamily">DejaVu Serif</xsl:param>
  <xsl:param
    name="bookmarkHeaders">false</xsl:param>
  <xsl:param
    name="bookmarkTables">false</xsl:param>
  <xsl:param 
    name="pageOrientation">portrait</xsl:param>
  <xsl:param 
    name="pageWidth">8.5in</xsl:param>
  <xsl:param 
    name="pageHeight">11in</xsl:param>

  <!--includes-->
  <xsl:include
    href="./includes/layout.xsl" />
  <xsl:include
    href="./includes/tables.xsl" />
  <xsl:include
    href="./includes/inline.xsl" />
  <xsl:include
    href="./includes/images.xsl" />
  <xsl:include
    href="./includes/structure.xsl" />
  <xsl:include
    href="./includes/block.xsl" />
  <xsl:include
    href="./includes/lists.xsl" />
  <xsl:include
    href="./includes/mathml.xsl" />

  <!-- attribute sets -->
  <!-- page geometry -->
  <xsl:attribute-set
    name="common_page_geometry">
    <xsl:attribute
      name="page-width">
      <xsl:value-of select="$pageWidth"/>
    </xsl:attribute>
    <xsl:attribute
      name="page-height">
      <xsl:value-of select="$pageHeight"/>
    </xsl:attribute>
    <xsl:attribute
      name="margin-left">
      <xsl:text>0.5in</xsl:text>
    </xsl:attribute>
    <xsl:attribute
      name="margin-right">
      <xsl:text>0.5in</xsl:text>
    </xsl:attribute>
    <xsl:attribute
      name="margin-top">
      <xsl:text>0.5in</xsl:text>
    </xsl:attribute>
    <xsl:attribute
      name="margin-bottom">
      <xsl:text>0.5in</xsl:text>
    </xsl:attribute>
  </xsl:attribute-set>

  <!-- page_numbers (header and/or footer landscape)-->
  <xsl:attribute-set
    name="page_numbers">
    <xsl:attribute
      name="font-family">
      <xsl:value-of
        select="$baseFontFamily" />
    </xsl:attribute>
    <xsl:attribute
      name="font-size">
      <xsl:text>1.25em</xsl:text>
    </xsl:attribute>
    <xsl:attribute
      name="padding">
      <xsl:text>6pt</xsl:text>
    </xsl:attribute>
    <xsl:attribute
      name="font-weight">
      <xsl:text>bold</xsl:text>
    </xsl:attribute>
    <xsl:attribute
      name="text-align">
      <xsl:text>right</xsl:text>
    </xsl:attribute>
    <!--<xsl:attribute
      name="start-indent">
      <xsl:text>80%</xsl:text>
    </xsl:attribute>
    <xsl:attribute
      name="border">
      <xsl:text>solid 2pt black</xsl:text>
    </xsl:attribute>-->
  </xsl:attribute-set>
  
  <!-- portrait-footer-common -->
  <xsl:attribute-set name="portrait-footer-common">
    <xsl:attribute name="font-family">
      <xsl:value-of select="$baseFontFamily" />
    </xsl:attribute>
    <xsl:attribute name="font-size">
      <xsl:text>1.25em</xsl:text>
    </xsl:attribute>
    <xsl:attribute name="padding">
      <xsl:text>6pt</xsl:text>
    </xsl:attribute>
    <xsl:attribute name="font-weight">
      <xsl:text>bold</xsl:text>
    </xsl:attribute>
    <xsl:attribute name="border-top">
      <xsl:text>solid 2pt black</xsl:text>
    </xsl:attribute>
  </xsl:attribute-set>

  <!-- inline page_numbers -->
  <xsl:attribute-set
    name="inline_page_numbers">
    <xsl:attribute
      name="padding">
      <xsl:text>4px</xsl:text>
    </xsl:attribute>
    <xsl:attribute
      name="break-before">
      <xsl:text>page</xsl:text>
    </xsl:attribute>
    <xsl:attribute
      name="space-before">
      <xsl:text>1em</xsl:text>
    </xsl:attribute>
    <xsl:attribute
      name="space-after">
      <xsl:text>1em</xsl:text>
    </xsl:attribute>
    <xsl:attribute
      name="font-size">
      <xsl:text>1.25em</xsl:text>
    </xsl:attribute>
    <xsl:attribute
      name="text-align">
      <xsl:text>center</xsl:text>
    </xsl:attribute>
    <xsl:attribute
      name="font-weight">
      <xsl:text>bold</xsl:text>
    </xsl:attribute>
    <xsl:attribute
      name="id">
      <xsl:value-of
        select="@id" />
    </xsl:attribute>
    <xsl:attribute
      name="border">
      <xsl:text>solid 2px black</xsl:text>
    </xsl:attribute>
  </xsl:attribute-set>

  <!-- lists -->
  <xsl:attribute-set
    name="list-geometry">
    <xsl:attribute
      name="space-before">0.5em</xsl:attribute>
    <xsl:attribute
      name="space-after">0.5em</xsl:attribute>
    <xsl:attribute
      name="provisional-distance-between-starts">24pt</xsl:attribute>
    <xsl:attribute
      name="provisional-label-separation">6pt</xsl:attribute>
  </xsl:attribute-set>
  <xsl:attribute-set
    name="list-item-geometry">
    <xsl:attribute
      name="space-after">0.5em</xsl:attribute>
  </xsl:attribute-set>
  <xsl:attribute-set
    name="list-item-label-geometry">
    <xsl:attribute
      name="end-indent">label-end()</xsl:attribute>
  </xsl:attribute-set>
  <xsl:attribute-set
    name="list-item-body-geometry">
    <xsl:attribute
      name="start-indent">body-start()</xsl:attribute>
  </xsl:attribute-set>

  <!-- block spacing -->
  <xsl:attribute-set
    name="spacing">
    <xsl:attribute
      name="space-after">0.5em</xsl:attribute>
    <xsl:attribute
      name="space-before">0.5em</xsl:attribute>
  </xsl:attribute-set>

  <!--SUP-->
  <xsl:attribute-set
    name="sup">
    <xsl:attribute
      name="font-size">smaller</xsl:attribute>
    <xsl:attribute
      name="baseline-shift">super</xsl:attribute>
    <xsl:attribute
      name="vertical-align">super</xsl:attribute>
    <xsl:attribute
      name="dominant-baseline">hanging</xsl:attribute>
    <xsl:attribute
      name="keep-together">always</xsl:attribute>
  </xsl:attribute-set>
  <!--SUB-->
  <xsl:attribute-set
    name="sub">
    <xsl:attribute
      name="font-size">smaller</xsl:attribute>
    <xsl:attribute
      name="baseline-shift">sub</xsl:attribute>
    <xsl:attribute
      name="dominant-baseline">hanging</xsl:attribute>
    <xsl:attribute
      name="keep-together">always</xsl:attribute>
  </xsl:attribute-set>

  <!-- Document level formating -->
  <xsl:template
    match="dtb:dtbook">
    <fo:block
      font-family="{$baseFontFamily}"
      font-size="{$baseFontSize}"
      line-height="{$lineHeight}"
      text-align="left">
      <xsl:apply-templates />
    </fo:block>
  </xsl:template>

</xsl:stylesheet>
