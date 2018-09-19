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
<p>The following Leave Request has been submitted by ${userName}.</p>
<p>Please <a href="https://hawaii2.execom.eu">login to the Hawaii system</a> to Approve or Reject the request.</p>
<table>
    <caption>Leave Request Details</caption>
    <tbody>
    <tr>
        <td class="left">Requested by</td>
        <td class="right">${userName}</td>
    </tr>
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
        <td class="right">${numberOfRequestedDays} days / ${numberOfRequestedDays*8} hours</td>
    </tr>
    <tr>
        <td class="left">Reason for leave</td>
        <td class="right">${reason}</td>
    </tr>
    </tbody>
</table>
<h3>Approval</h3>
<p>Your approval for this request is <b>required.</b></p>
<br>
<br>
<hr>
<p class="footer">This is a notification email from the Hawaii HR system. Please do net reply directly to this email.</p>
</body>
</html>
