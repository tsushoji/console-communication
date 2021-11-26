# console-communication

# 使い方
## 1. UDP通信設定ファイル作成  
### 「resources」フォルダ以下に「communication.properties」ファイルを作成。  
### フォーマットは以下とする。  
* ユニキャスト  
udp.send.addres-送信アドレス  
udp.send.port-送信ポート  
udp.receive.port-受信ポート  
* マルチキャスト  
udp.multicast.addres-マルチキャストアドレス  
udp.multicast.send.port-送信ポート  
udp.multicast.send.port-受信ポート  
## 2. コンソールアプリメニュー  
### 「操作したいモードを選んでください:」から以下モードを選択し、Enterキー。  
* 0→ユニキャスト  
※上記モード選択メニューに戻る場合、「#q」を入力し、Enterキー。  
通信設定について、以下とする。  
送信アドレス→1の「udp.send.addres」  
送信ポート→1の「udp.send.port」  
受信ポート→1の「udp.receive.port」  
* 1→マルチキャスト  
※定時送信あり  
※上記モード選択メニューに戻る場合、「#q」を入力し、Enterキー。  
通信設定について、以下とする。  
マルチキャストアドレス→1の「udp.multicast.addres」  
送信ポート→1の「udp.multicast.send.port」  
受信ポート→1の「udp.multicast.send.port」  
* e→終了  

