// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Base;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.NeoBase;

public class RotateToHeading extends CommandBase {
  private final NeoBase base;
  private double heading;

  public RotateToHeading(NeoBase base, double heading) {
    this.base = base;
    this.heading = heading;
    addRequirements(base);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    base.rotateToHeading(heading);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return base.getHeadingDeg() <= heading + 3 && base.getHeadingDeg() >= heading - 3;
  }
}
