// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;

public class Agitate extends SequentialCommandGroup {

  public Agitate(Intake intake) {
    addCommands(
      new WaitCommand(1.5),
      new InstantCommand(
        () -> intake.setIntakePivotPose(Constants.IntakeConstants.RESET_PIVOT_POSITION), intake
      ),
      new WaitCommand(0.25),
      new InstantCommand(
        () -> intake.setIntakePivotPose(Constants.IntakeConstants.CARRY_PIVOT_POSITION), intake
      )
    );
  }
}