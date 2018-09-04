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
<p>This is a notification from the Hawaii system.</p>
<p>A sickness report for ${userName} has been submitted.</p>
<h4>${numberOfRequestedDays} day/s on requested dates ${startDate} - ${endDate}</h4>
<p>You are receiving this notification because you are listed as a Notifier for the ${teamName} team. To be removed from this list, please contact your Hawaii Administrator.</p>
<table>
    <caption>Sickness Report Details</caption>
    <tbody>
    <tr>
        <td class="left">Requested by</td>
        <td class="right">${userName}</td>
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
        <td class="left">Reason for sickness</td>
        <td class="right">${reason}</td>
    </tr>
    </tbody>
</table>
<p>For further information, <a href="https://hawaii2.execom.eu">click here</a> to open the request.</p>
<br>
<hr>
<p class="footer">This is a notification email from the Hawaii HR system. Please do net reply directly to this
    email.</p>
</body>
</html>