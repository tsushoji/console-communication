# console-communication

# 使い方
## 1. UDP・TCP通信設定ファイル作成  
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
* サーバー  
tcp.server.port-リッスンポート  
* クライアント  
tcp.client.addres-送信アドレス  
tcp.client.port-送信ポート  
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
* 2→サーバーリッスン開始  
※1対1のみ対応  
※コメント「TCP-S-①」の第2引数で受信バイナリーデータサイズ、第3引数でレスポンスバイナリーデータサイズを渡す  
※コメント「TCP-S-➁」はクライアントからのデータ受信後、コールバック処理用インターフェース実装例  
上記について、コメント「TCP-S-➂」、「TCP-S-④」メソッドオーバーライド必要  
※上記モード選択メニューに戻る場合、「#q」を入力し、Enterキー。  
通信設定について、以下とする。  
リッスンポート→1の「tcp.server.port=10007」  
* 3→クライアント送信  
※コメント「TCP-C-①」は送信データチェック処理  
※コメント「TCP-C-➁」の第2引数でレスポンスデータサイズを渡す  
※上記モード選択メニューに戻る場合、「#q」を入力し、Enterキー。  
通信設定について、以下とする。  
送信アドレス→1の「tcp.client.addres」  
送信ポート→1の「tcp.client.port」  
* e→終了  

