/*	Este arquivo é parte do programa MonitorLog

    MonitorLog é um software livre; você pode redistribui-lo e/ou 
    modificá-lo dentro dos termos da Licença Pública Geral GNU como 
    publicada pela Fundação do Software Livre (FSF); na versão 2 da 
    Licença, ou qualquer versão.

    Este programa é distribuído na esperança que possa ser  útil, 
    mas SEM NENHUMA GARANTIA; sem uma garantia implícita de ADEQUAÇÂO a qualquer
    MERCADO ou APLICAÇÃO EM PARTICULAR. Veja a

    Licença Pública Geral GNU para maiores detalhes.
    Você deve ter recebido uma cópia da Licença Pública Geral GNU
    junto com este programa, se não, escreva para a Fundação do Software
    Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
    
 */
package service.widget;

public interface MonitorServiceInterface {

	public String getBatteryCapacity();

	public String getBatteryStatus();

	public String getDate();

	public String getTimestamp();

	public String getTaxaTotalTx();

	public String getTaxaTotalRx();

	public String getTaxaMobileTx();

	public String getTaxaMobileRx();

	public String getGPS();

	public String getWiFiLinkSpeed();

	public String getWiFiRSSI();

	public String getAccelerometer();

	public String getTypeNameConnection();

	public long getTimeThread();

	public void execute();

}
