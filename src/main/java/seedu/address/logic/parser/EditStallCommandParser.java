package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STALL;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditStallCommand;
import seedu.address.logic.parser.exceptions.ParseException;



/**
 * Parses input arguments and creates a new EditStallCommand object
 */
public class EditStallCommandParser implements Parser<EditStallCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditStallCommand
     * and returns an EditStallCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditStallCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_STALL, PREFIX_NAME, PREFIX_LOCATION);

        if (!arePrefixesPresent(argMultimap, PREFIX_STALL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditStallCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STALL, PREFIX_NAME, PREFIX_LOCATION);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_STALL).get());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditStallCommand.MESSAGE_USAGE), pe);
        }
        EditStallCommand.EditStallDescriptor editStallDescriptor = new EditStallCommand.EditStallDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editStallDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }

        if (argMultimap.getValue(PREFIX_LOCATION).isPresent()) {
            editStallDescriptor.setLocation(ParserUtil.parseLocation(argMultimap.getValue(PREFIX_LOCATION).get()));
        }

        if (!editStallDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditStallCommand.MESSAGE_NOT_EDITED);
        }

        return new EditStallCommand(index, editStallDescriptor);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
