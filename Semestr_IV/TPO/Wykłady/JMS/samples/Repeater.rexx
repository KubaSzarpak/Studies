/**/
sleeps.1 = 5
sleeps.2 = 15
sleeps.3 = 20
msg.1 = "Obiad!"
msg.2 = "Wyslij list"
msg.3 = "Dobranoc"
do i = 1 to 3
  call sleep sleeps.i
  call "run.bat Sender queue1 " msg.i
end
say 'Ok'

