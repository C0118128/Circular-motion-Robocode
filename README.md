# 円運動型回避行動 Robocode ~ Circular-motion-Robocode ~

## 概要
Robocodeを利用した自作ロボット。敵ロボットを捕捉して、対象を中心と円運動を行う。

## 機能&技術
* 敵ロボット捕捉 </br>
  最も近距離の敵ロボットを回避行動の対象とする。
* 被弾時・敵ロボット・壁との衝突時の処理 </br>
  非常時の回避行動をとる。
* 距離に応じた攻撃威力の変更 </br>
  威力を変更することで自滅を軽減する。

## 長所&短所
長所
* 円運動を行うことで敵の攻撃を高い確率で回避できる。
* 敵ロボットの動きに回避率は左右されない。
短所
* 回避を意識したロボットのため、攻撃が敵ロボットに当たる確率が低い。

## デモ

## インストール方法
Roboode内で本ロボットを登録して、実行してください。 </br>
`$ git clone https://github.com/C0118128/Circular-motion-Robocode.git`

## 作者
* Ryoto

## ライセンス
This sample code is under [MIT license](https://en.wikipedia.org/wiki/MIT_License).
