package main;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RadarTurnCompleteCondition;
import robocode.ScannedRobotEvent;
import robocode.TurnCompleteCondition;

public class circularMotion extends AdvancedRobot {
	private int loop = 0;
	private int saveLoop = 0;
	private int Velocity = 5;
	private int direction = 1;
	private boolean onSentryArea = false;
	private String targetName = "";
	private Map<String, Double> enemysMap = new HashMap<>();
	private int enemyNum = 0;
	private int lostNum = 0;


	private void targetSelect(ScannedRobotEvent event) {
		String shortRangeEnemyName = "";
		double shortRangeEnemyeDistance = 10000;


		if(event.getName().equals("samplesentry.BorderGuard")) {
		}else {
			enemysMap.put(event.getName(), event.getDistance());
		}
		if(enemyNum != enemysMap.size()) {
			enemyNum = enemysMap.size();
		}else {
			Set<Map.Entry<String, Double>> enemys = enemysMap.entrySet();
			for(Map.Entry<String, Double> enemy: enemys) {
				String name = enemy.getKey();
				double distance = enemy.getValue();

				System.out.println(name);

				if(distance < shortRangeEnemyeDistance) {
					shortRangeEnemyName = name;
					shortRangeEnemyeDistance = distance;
				}
			}
			targetName = shortRangeEnemyName;

			System.out.println("enemyNum: " + enemyNum + "/" + "set: "+targetName);

			enemysMap.clear();
			enemyNum = 0;
		}

	}

	private void reTargetSelect() {
		targetName = "";
		lostNum = 0;
		setTurnRadarRight(720);
		execute();
	}

	private void directionSwitch() {
		direction *= -1;
	}

	private void reverseOperation() {
		directionSwitch();
		Velocity = 8;
		setMaxVelocity(Velocity);
		saveLoop = loop;
	}

	private void retrunVelocity() {
		if(saveLoop+2 == loop) {
			Velocity = 5;
			setMaxVelocity(Velocity);
		}
	}

	private boolean intoSentryBorder() {
		boolean result = false;
		if(getY() < getSentryBorderSize()+80 || getY() > getBattleFieldHeight()-getSentryBorderSize()-80
				|| getX() < getSentryBorderSize()+80 || getX() > getBattleFieldWidth()-getSentryBorderSize()-80){
			if(onSentryArea == false){
				onSentryArea = true;
				result = true;
			}
		}else {
			if(onSentryArea == true) {
				onSentryArea = false;
			}
		}
		return result;
	}


	@Override
	public void run() {
		this.setAllColors(Color.BLACK);
		this.setBulletColor(Color.YELLOW);
		this.setScanColor(Color.RED);

		setTurnGunRight(45);
		setTurnRadarRight(720);
		setMaxVelocity(Velocity);

		waitFor(new RadarTurnCompleteCondition(this));
		waitFor(new TurnCompleteCondition(this));
		while(true) {
			if(intoSentryBorder() == true) {
				reverseOperation();
			}
			setAhead(direction * 60);
			lostNum += 1;
			turnGunRight(90);
			execute();

			if(intoSentryBorder() == true) {
				reverseOperation();
			}
			setAhead(direction * 60);
			lostNum += 1;
			turnGunRight(-90);
			execute();

			if(lostNum >= 4) {
				reTargetSelect();

				System.out.println("lost");
			}
			retrunVelocity();
			loop++;
		}


	}

	@Override
	public void onHitByBullet(HitByBulletEvent event) {
		reverseOperation();
	}

	@Override
	public void onHitRobot(HitRobotEvent event) {
		reverseOperation();
	}

	@Override
	public void onHitWall(HitWallEvent event) {
		reverseOperation();
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		if(targetName.equals("")) {
			targetSelect(event);
		}else {
			double power = 1.0;
			if (event.getDistance() < 150) {
				power = 3.0;
				setFire(power);
			}else if(event.getDistance() < 300){
				power = 2.0;
				setFire(power);
			}

			if(event.getName().equals(targetName)) {
				setTurnRight(event.getBearing() - 90);
				lostNum = 0;
			}
			execute();
		}
	}

}