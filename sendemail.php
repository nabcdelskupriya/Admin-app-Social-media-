<?php  
$name = $_POST['name'];
$email = $_POST['email'];
$message = $_POST['message'];

$email_subject = "Social media app";

$email_admin = "rajjansharma669@gmail.com";

$email_body = "Dear $name \n".
                    "$message \n";

   $headers = "Form: $email_admin \r\n";
   $headers = "Reply-To: $email_admin \r\n";

   mail($email, $email_subject,$email_body,$headers);

?>