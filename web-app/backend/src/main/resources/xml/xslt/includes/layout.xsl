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
<xsl:stylesheet exclude-result-prefixes="dtb" version="1.0"
  xmlns:dtb="http://www.daisy.org/z3986/2005/dtbook/" xmlns:fo="http://www.w3.org/1999/XSL/Format"
  xmlns:fox="http://xmlgraphics.apache.org/fop/extensions"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">




  <xsl:template match="/">
    <fo:root>
      <!--layout-master-set-->
      <fo:layout-master-set>

        <!--layout for first recto page-->
        <fo:simple-page-master margin-top="1in" master-name="cover"
          xsl:use-attribute-sets="common_page_geometry">
          <fo:region-body />
        </fo:simple-page-master>

        <!--layout for even top verso pages (landscape)-->
        <fo:simple-page-master master-name="top-page"
          xsl:use-attribute-sets="common_page_geometry">
          <fo:region-body margin-bottom="0.75in" margin-top="0.75in"/>
          <fo:region-before extent="0.5in" precedence="true" region-name="top-header" />
        </fo:simple-page-master>
        <!-- layout for odd bottom recto pages (landscape)-->
        <fo:simple-page-master master-name="bottom-page"
          xsl:use-attribute-sets="common_page_geometry">
          <fo:region-body margin-top="0.75in" margin-bottom="0.75in"/>
          <fo:region-after extent="0.5in" precedence="true" region-name="bottom-footer" />
        </fo:simple-page-master>

        <!-- layout for left verso pages (portrait)-->
        <fo:simple-page-master master-name="left-page"
          xsl:use-attribute-sets="common_page_geometry">
          <fo:region-body margin-right="0.75in" margin-bottom="0.75in" />
          <fo:region-after extent="0.5in" precedence="true" region-name="left-footer"/>
        </fo:simple-page-master>
        <!-- layout for right recto pages (portrait)-->
        <fo:simple-page-master master-name="right-page"
          xsl:use-attribute-sets="common_page_geometry">
          <fo:region-body margin-left="0.75in" margin-bottom="0.75in" />
          <fo:region-after extent="0.5in" precedence="true" region-name="right-footer"/>
        </fo:simple-page-master>

        <!--page sequence master-->
        <xsl:choose>
          <xsl:when test="$pageOrientation='landscape'">
            <fo:page-sequence-master master-name="landscape">
              <fo:repeatable-page-master-alternatives>
                <fo:conditional-page-master-reference master-reference="cover" page-position="first" />
                <fo:conditional-page-master-reference master-reference="top-page" odd-or-even="even" />
                <fo:conditional-page-master-reference master-reference="bottom-page" odd-or-even="odd" />
              </fo:repeatable-page-master-alternatives>
            </fo:page-sequence-master>
          </xsl:when>
          <xsl:otherwise>
            <fo:page-sequence-master master-name="portrait">
              <fo:repeatable-page-master-alternatives>
                <fo:conditional-page-master-reference master-reference="cover" page-position="first" />
                <fo:conditional-page-master-reference master-reference="left-page" odd-or-even="even" />
                <fo:conditional-page-master-reference master-reference="right-page" odd-or-even="odd" />
              </fo:repeatable-page-master-alternatives>
            </fo:page-sequence-master>
          </xsl:otherwise>
        </xsl:choose>


      </fo:layout-master-set>

      <!-- bookmark section -->
      <xsl:if test="$bookmarkHeaders='true'">
        <fo:bookmark-tree>
          <xsl:for-each select="/dtb:dtbook/dtb:book//dtb:level1/dtb:h1">
            <fo:bookmark internal-destination="{generate-id(.)}">
              <fo:bookmark-title>
                <xsl:value-of select="normalize-space(./text())" />
              </fo:bookmark-title>
              <xsl:for-each select="parent::node()/descendant::dtb:h2">
                <fo:bookmark internal-destination="{generate-id(.)}">
                  <fo:bookmark-title>
                    <xsl:value-of select="normalize-space(./text())" />
                  </fo:bookmark-title>
                </fo:bookmark>
              </xsl:for-each>
            </fo:bookmark>
          </xsl:for-each>
          <xsl:if test="$bookmarkTables='true'">
            <xsl:for-each select="/dtb:dtbook/dtb:book//dtb:table">
              <fo:bookmark internal-destination="{generate-id(.)}">
                <fo:bookmark-title>
                  <xsl:text>Table: </xsl:text>
                  <xsl:value-of select="./@id" />
                </fo:bookmark-title>
              </fo:bookmark>
            </xsl:for-each>
          </xsl:if>
        </fo:bookmark-tree>
      </xsl:if>
      
      <xsl:choose>
        <xsl:when test="$pageOrientation='landscape'">
          <!--LANDSCAPE PAGE SEQUENCE-->
      <fo:page-sequence id="landscape{generate-id(.)}" initial-page-number="1" master-reference="landscape">
        <!--Top Header(landscape)-->
        <fo:static-content flow-name="top-header">
          <fo:block xsl:use-attribute-sets="page_numbers" border-bottom="solid 2pt black">
            <fo:retrieve-marker retrieve-class-name="pn" retrieve-position="last-ending-within-page"
             />
          </fo:block>
        </fo:static-content>
        <!--Bottom Footer (landscape)-->
        <fo:static-content flow-name="bottom-footer">
          <fo:block xsl:use-attribute-sets="page_numbers" border-top="solid 2pt black">
            <fo:retrieve-marker retrieve-class-name="pn" retrieve-position="last-ending-within-page"
             />
          </fo:block>
        </fo:static-content>
        <!--Body-->
        <fo:flow flow-name="xsl-region-body">
          <xsl:apply-templates />
        </fo:flow>
      </fo:page-sequence>
        </xsl:when>
        <xsl:otherwise>
          <!-- PORTRAIT PAGE SEQUENCE -->
      <fo:page-sequence id="portrait{generate-id(.)}" master-reference="portrait">
        <fo:static-content flow-name="left-footer">
          <fo:block xsl:use-attribute-sets="portrait-footer-common" text-align="left">
            <fo:retrieve-marker retrieve-class-name="pn" retrieve-position="last-ending-within-page"/>
          </fo:block>
        </fo:static-content>
        <fo:static-content flow-name="right-footer">
          <fo:block xsl:use-attribute-sets="portrait-footer-common" text-align="right">
            <fo:retrieve-marker retrieve-class-name="pn" retrieve-position="last-ending-within-page"/>
          </fo:block>
        </fo:static-content>
        <!--Body-->
        <fo:flow flow-name="xsl-region-body">
          <xsl:apply-templates />
        </fo:flow>
      </fo:page-sequence>

        </xsl:otherwise>
      </xsl:choose>
      

      
    </fo:root>
  </xsl:template>
</xsl:stylesheet>
