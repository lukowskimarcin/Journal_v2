package com.journal.journal.cmd.api.commands;

 
import com.journal.cqrses.commands.BaseCommand;

import lombok.Data;

@Data
public class WithdrawFundsCommand extends BaseCommand {
    private double amount;
}
