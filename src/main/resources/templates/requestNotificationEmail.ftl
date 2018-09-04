<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
      <#include "emailStyle.css">
    </style>
</head>
<body>
<p>Hi, ${userName}.</p>
<p>The following Leave Request has been <b style="text-transform: uppercase;">${status}</b></p>
<table>
    <caption>Leave Request Details</caption>
    <tbody>
    <tr>
        <td class="left">Leave type</td>
        <td class="right">${absenceName}</td>
    </tr>
    <tr>
        <td class="left">Requested dates</td>
        <td class="right">${startDate} - ${endDate}</td>
    </tr>
    <tr>
        <td class="left">Time Requested</td>
        <td class="right">${numberOfRequestedDays}</td>
    </tr>
    <tr>
        <td class="left">Reason for leave</td>
        <td class="right">${reason}</td>
    </tr>
    </tbody>
</table>
<p>For further information, <a href="https://hawaii2.execom.eu">click here</a> to open the request.</p>
<br>
<br>
<hr>
<p class="footer">This is a notification email from the Hawaii HR system. Please do net reply directly to this
    email.</p>
</body>
</html>
