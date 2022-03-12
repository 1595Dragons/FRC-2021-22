// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AutoCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AutoShootHigh extends CommandBase {
  
  Timer time;
  Boolean finish = true;

  ShooterSubsystem m_shooterSubsystem;
  IntakeSubsystem m_intakeSubsystem;
  public AutoShootHigh(ShooterSubsystem m_shooterSubsystem, IntakeSubsystem m_intakeSubsystem) {
    this.m_shooterSubsystem = m_shooterSubsystem;
    this.m_intakeSubsystem = m_intakeSubsystem;
    addRequirements(m_shooterSubsystem);
    addRequirements(m_intakeSubsystem);
    time = new Timer();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    time.reset();
    time.start();
    while(time.get() < Constants.autoShootTime) {
      m_shooterSubsystem.shootHigh(Constants.shootHigh);
      if (time.get() > .5) {
        m_intakeSubsystem.ejectBallsToShoot();
      }
    }
  }

  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finish;
  }
}
