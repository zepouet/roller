import org.crsh.cli.Usage
import org.crsh.cli.Command

class roller
{
  @Usage("Roller base command that does nothing")
  @Command
  void main() {
  	def factory = org.apache.roller.weblogger.business.WebloggerFactory.getWeblogger();
  	out << "The roller factory ${factory}";
 }
}