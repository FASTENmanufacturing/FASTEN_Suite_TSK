<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" 
 xmlns:v="urn:schemas-microsoft-com:vml"
 xmlns:o="urn:schemas-microsoft-com:office:office">
<head>
  <!--[if gte mso 9]><xml>
   <o:OfficeDocumentSettings>
    <o:AllowPNG/>
    <o:PixelsPerInch>96</o:PixelsPerInch>
   </o:OfficeDocumentSettings>
  </xml><![endif]-->
  <!-- fix outlook zooming on 120 DPI windows devices -->
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1"> <!-- So that mobile will display zoomed in -->
  <meta http-equiv="X-UA-Compatible" content="IE=edge"> <!-- enable media queries for windows phone 8 -->
  <meta name="format-detection" content="date=no"> <!-- disable auto date linking in iOS 7-9 -->
  <meta name="format-detection" content="telephone=no"> <!-- disable auto telephone linking in iOS 7-9 -->
  <title>Two Columns to Rows (Simple)</title>
  
  <style type="text/css">
body {
  margin: 0;
  padding: 0;
  -ms-text-size-adjust: 100%;
  -webkit-text-size-adjust: 100%;
}

table {
  border-spacing: 0;
}

table td {
  border-collapse: collapse;
}

.ExternalClass {
  width: 100%;
}

.ExternalClass,
.ExternalClass p,
.ExternalClass span,
.ExternalClass font,
.ExternalClass td,
.ExternalClass div {
  line-height: 100%;
}

.ReadMsgBody {
  width: 100%;
  background-color: #ebebeb;
}

table {
  mso-table-lspace: 0pt;
  mso-table-rspace: 0pt;
}

img {
  -ms-interpolation-mode: bicubic;
}

.yshortcuts a {
  border-bottom: none !important;
}

@media screen and (max-width: 599px) {
  .force-row,
  .container {
    width: 100% !important;
    max-width: 100% !important;
  }
}
@media screen and (max-width: 400px) {
  .container-padding {
    padding-left: 12px !important;
    padding-right: 12px !important;
  }
}
.ios-footer a {
  color: #aaaaaa !important;
  text-decoration: underline;
}
a[href^="x-apple-data-detectors:"],
a[x-apple-data-detectors] {
  color: inherit !important;
  text-decoration: none !important;
  font-size: inherit !important;
  font-family: inherit !important;
  font-weight: inherit !important;
  line-height: inherit !important;
}
</style>
</head>

<body style="margin:0; padding:0;" bgcolor="#F0F0F0" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<!-- 100% background wrapper (grey background) -->
<table border="0" width="100%" height="100%" cellpadding="0" cellspacing="0" bgcolor="#F0F0F0">
  <tr>
    <td align="center" valign="top" bgcolor="#F0F0F0" style="background-color: #F0F0F0;">

      <br>

      <!-- 600px container (white background) -->
      <table border="0" width="600" cellpadding="0" cellspacing="0" class="container" style="width:600px;max-width:600px">
        <tr>
          <td class="container-padding header" align="left" style="font-family:Helvetica, Arial, sans-serif;font-size:24px;font-weight:bold;padding-bottom:12px;color:#DF4726;padding-left:24px;padding-right:24px">
            Antwort v1.0
          </td>
        </tr>
        <tr>
          <td class="container-padding content" align="left" style="padding-left:24px;padding-right:24px;padding-top:12px;padding-bottom:12px;background-color:#ffffff">
            <br>
<div class="title" style="font-family:Helvetica, Arial, sans-serif;font-size:18px;font-weight:600;color:#374550">Two Columns (simple)</div>
<br>

<div class="body-text" style="font-family:Helvetica, Arial, sans-serif;font-size:14px;line-height:20px;text-align:left;color:#333333">
  This is an example of transforming two columns on desktop into rows on mobile.
  <br><br>
</div>

<div class="hr" style="height:1px;border-bottom:1px solid #cccccc">&nbsp;</div>
<br>

<!-- example: two columns (simple) -->

<!--[if mso]>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr><td width="50%" valign="top"><![endif]-->

    <table width="264" border="0" cellpadding="0" cellspacing="0" align="left" class="force-row">
      <tr>
        <td class="col" valign="top" style="font-family:Helvetica, Arial, sans-serif;font-size:14px;line-height:20px;text-align:left;color:#333333;width:100%">
          <strong>Herman Melville</strong>
          <br><br>
          It's worse than being in the whirled woods, the last day of the year! Who'd go climbing after chestnuts now? But there they go, all cursing, and here I don't.
          <br><br>
        </td>
      </tr>
    </table>

    <!--[if mso]></td><td width="50%" valign="top"><![endif]-->

    <table width="264" border="0" cellpadding="0" cellspacing="0" align="right" class="force-row">
      <tr>
        <td class="col" valign="top" style="font-family:Helvetica, Arial, sans-serif;font-size:14px;line-height:20px;text-align:left;color:#333333;width:100%">
        <strong>I am Ishmael</strong>
        <br><br>
        White squalls? white whale, shirr! shirr! Here have I heard all their chat just now, and the white whale—shirr! shirr!—but spoken of once! and…
        <br><br>
        </td>
      </tr>
    </table>

<!--[if mso]></td></tr></table><![endif]-->


<!--/ end example -->

<div class="hr" style="height:1px;border-bottom:1px solid #cccccc;clear: both;">&nbsp;</div>
<br>

<div class="subtitle" style="font-family:Helvetica, Arial, sans-serif;font-size:16px;font-weight:600;color:#2469A0">
  Code Walkthrough
</div>

<div class="body-text" style="font-family:Helvetica, Arial, sans-serif;font-size:14px;line-height:20px;text-align:left;color:#333333">
  <ol>
    <li>
      Create a columns container <code style="background-color:#eee;padding:0 4px;font-family:Menlo, Courier, monospace;font-size:12px">&lt;table&gt;</code> just for Outlook using <code style="background-color:#eee;padding:0 4px;font-family:Menlo, Courier, monospace;font-size:12px">if mso</code> conditionals.<br>
      The container's <code style="background-color:#eee;padding:0 4px;font-family:Menlo, Courier, monospace;font-size:12px">&lt;td&gt;</code>'s have a width of <code style="background-color:#eee;padding:0 4px;font-family:Menlo, Courier, monospace;font-size:12px">50%.</code>
      <br><br>
    </li>
    <li>
      Wrap our columns in a <code style="background-color:#eee;padding:0 4px;font-family:Menlo, Courier, monospace;font-size:12px">&lt;table&gt;</code> with a <strong>fixed width</strong> of <code style="background-color:#eee;padding:0 4px;font-family:Menlo, Courier, monospace;font-size:12px">264px</code>.
      <ul>
        <li>
          264px = (600 - 24*3) / 2 - container width minus margins divided by number of columns
        </li>
        <li>
          First table is aligned left.
        </li>
        <li>
          Second table is aligned right.
        </li>
      </ul>
      <br>
    </li>
    <li>
      Apply <code style="background-color:#eee;padding:0 4px;font-family:Menlo, Courier, monospace;font-size:12px">clear: both;</code> to first element after our wrapper table.
    </li>
  </ol>

  <br>
  <em><small>Last updated: 10 October 2014</small></em>
</div>

<br>
          </td>
        </tr>
        <tr>
          <td class="container-padding footer-text" align="left" style="font-family:Helvetica, Arial, sans-serif;font-size:12px;line-height:16px;color:#aaaaaa;padding-left:24px;padding-right:24px">
            <br><br>
            Sample Footer text: © 2015 Acme, Inc.
            <br><br>

            You are receiving this email because you opted in on our website. Update your <a href="#" style="color:#aaaaaa">email preferences</a> or <a href="#" style="color:#aaaaaa">unsubscribe</a>.
            <br><br>

            <strong>Acme, Inc.</strong><br>
            <span class="ios-footer">
              123 Main St.<br>
              Springfield, MA 12345<br>
            </span>
            <a href="http://www.acme-inc.com" style="color:#aaaaaa">www.acme-inc.com</a><br>

            <br><br>

          </td>
        </tr>
      </table>
<!--/600px container -->


    </td>
  </tr>
</table>
<!--/100% background wrapper-->

</body>
</html>
